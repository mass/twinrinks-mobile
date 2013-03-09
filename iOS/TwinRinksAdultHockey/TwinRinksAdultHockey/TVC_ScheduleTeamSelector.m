#import "TVC_ScheduleTeamSelector.h"
#import "MemoryManager.h"
#import "Team.h"
#import "VC_ScheduleGames.h"

@interface TVC_ScheduleTeamSelector ()

@property (strong, nonatomic) NSArray *teamsArray;
@property (strong, nonatomic) NSArray *leagueArray;
@property (strong, nonatomic) NSArray *arrayOfTeamArrays;

@end

@implementation TVC_ScheduleTeamSelector

-(id)initWithStyle:(UITableViewStyle)style {
    self = [super initWithStyle:style];
    if (self) {}
    return self;
}

-(void)viewDidLoad {
    [super viewDidLoad];
    MemoryManager *myManger = [[MemoryManager alloc]init];
    _teamsArray = [myManger getTeamArray];
    _leagueArray = [self generateLeagueArray];
    _arrayOfTeamArrays = [self generateDoubleArray];
    self.tableView.backgroundView = nil;
    self.view.backgroundColor = [UIColor colorWithRed:0.2f green:0.2f blue:0.2f alpha:1.0f];
}

-(NSArray *) generateDoubleArray {
    NSMutableArray *mutable = [[NSMutableArray alloc]init];
    for(int i=0;i<_leagueArray.count;i++)
        [mutable addObject:[[NSMutableArray alloc]init]];
    
    for(int i=0;i<_teamsArray.count;i++)
        for(int j=0;j<_leagueArray.count;j++)
            if([((Team *)[_teamsArray objectAtIndex:i]).league isEqualToString:((NSString *) [_leagueArray objectAtIndex:j])])
                if(![[mutable objectAtIndex:j] containsObject:((Team *) [_teamsArray objectAtIndex:i])])
                    [[mutable objectAtIndex:j] addObject:((Team *) [_teamsArray objectAtIndex:i])];
    
    return [[NSArray alloc] initWithArray:mutable];
}

-(int)getNumberOfTeamsInLeague:(NSString *)league {
    int count=0;
    for(int i=0;i<_teamsArray.count;i++)
        if([((Team *)[_teamsArray objectAtIndex:i]).league isEqualToString:league])
            count++;
    
    return count;
}

-(NSArray *)generateLeagueArray {
    NSMutableArray *mutable = [[NSMutableArray alloc]init];
    for(int i=0;i<_teamsArray.count;i++) 
        if(![mutable containsObject:((Team *)[_teamsArray objectAtIndex:i]).league])
            [mutable addObject:((Team *)[_teamsArray objectAtIndex:i]).league];
    
    return [[NSArray alloc] initWithArray:mutable];
}

#pragma mark - Table view data source

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return _leagueArray.count;
}

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *tempView=[[UIView alloc]initWithFrame:CGRectMake(0,200,300,244)];
    tempView.backgroundColor=[UIColor clearColor];
    
    UILabel *tempLabel=[[UILabel alloc]initWithFrame:CGRectMake(15,0,300,44)];
    tempLabel.backgroundColor=[UIColor clearColor];
    tempLabel.shadowColor = [UIColor blackColor];
    tempLabel.shadowOffset = CGSizeMake(0,2);
    tempLabel.textColor = [UIColor whiteColor];
    tempLabel.font = [UIFont fontWithName:@"Helvetica" size:18.0f];
    tempLabel.font = [UIFont boldSystemFontOfSize:18.0f];
    tempLabel.text=[_leagueArray objectAtIndex:section];
    [tempView addSubview:tempLabel];
    
    return tempView;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self getNumberOfTeamsInLeague:((NSString *)[_leagueArray objectAtIndex:section])];
}

-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return [_leagueArray objectAtIndex:section];
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"id_cell_scheduleTeamSelector";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    
    if (cell == nil) 
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    
    Team *temp = [[self.arrayOfTeamArrays objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
    ((UILabel *)[cell viewWithTag:0]).text = [temp teamName];
    
    return cell;
}

#pragma mark - Table view delegate

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    VC_ScheduleGames *vc_ScheduleGames = [self.storyboard instantiateViewControllerWithIdentifier:@"id_vc_scheduleGames"];
    vc_ScheduleGames.dataToDisplay = [((Team *) [[self.arrayOfTeamArrays objectAtIndex:indexPath.section] objectAtIndex:indexPath.row]) getTeamKey];
    [self.navigationController pushViewController:vc_ScheduleGames animated:YES];
}

@end