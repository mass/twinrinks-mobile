#import "Team.h"
#import "MemoryManager.h"

@interface TVC_Settings : UITableViewController

@property (strong, nonatomic) NSArray *yourTeamsArray;
@property (strong, nonatomic) NSString *lastSelectedTeam;
@property (strong, nonatomic) MemoryManager *myManager;

@end

@implementation TVC_Settings

-(id)initWithStyle:(UITableViewStyle)style {
    self = [super initWithStyle:style];
    if (self) {}
    return self;
}

-(void)viewDidLoad {
    [super viewDidLoad];
    
    _myManager = [[MemoryManager alloc] init];
    _yourTeamsArray = [_myManager getYourTeamArray];
}

#pragma mark - Table view data source

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _yourTeamsArray.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"id_cell_yourTeams";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    
    if (cell == nil) 
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    
    UILabel *label = (UILabel *)[cell viewWithTag:0];
    Team *temp = ((Team *)[_yourTeamsArray objectAtIndex:indexPath.row]);
    label.text = [temp toString];
    
    return cell;
}

#pragma mark - Table view delegate

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSString *toAppend = [((Team *) [_yourTeamsArray objectAtIndex:indexPath.row]) toString];
    _lastSelectedTeam = toAppend;
    
    NSString *message = [@"Do you want to remove this team from your teams: " stringByAppendingString:toAppend];
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Remove?" message:message delegate:self cancelButtonTitle:@"No" otherButtonTitles:@"Yes", nil];
    [alert show];
}

-(void)alertView:(UIAlertView *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 1) {
        NSMutableArray *mutable = [NSMutableArray arrayWithArray:_yourTeamsArray];
        for(int i=mutable.count-1;i>=0;i--)
            if([[((Team *)[mutable objectAtIndex:i]) toString] isEqualToString:_lastSelectedTeam])
               [mutable removeObjectAtIndex:i];
        
        [_myManager saveYourTeamsArray:[NSArray arrayWithArray:mutable]];
        _yourTeamsArray = [_myManager getYourTeamArray];
        
        [self.tableView reloadData];
    }
}

@end