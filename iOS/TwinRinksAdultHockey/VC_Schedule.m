#import "VC_ScheduleGames.h"
#import "TVC_ScheduleTeamSelector.h"
#import "TVC_ScheduleYourTeamSelector.h"
#import "MemoryManager.h"

@interface VC_Schedule : UITableViewController

-(IBAction)refreshData:id;
-(IBAction)settingsButtonPressed:id;
-(void)initButtons;

@end

@implementation VC_Schedule

-(id)initWithStyle:(UITableViewStyle)style {
    self = [super initWithStyle:style];
    if (self) {}
    return self;
}

-(void)viewDidLoad {
    [super viewDidLoad];
    [self initButtons];
    
    self.tableView.backgroundView = nil;
    self.view.backgroundColor = [UIColor colorWithRed:0.2f green:0.2f blue:0.2f alpha:1.0f];
}

-(IBAction)refreshData:(id)sender {
    MemoryManager *myManager = [[MemoryManager alloc]init];
    [myManager refreshData];
}

-(IBAction)settingsButtonPressed:(id)sender {
    UIViewController *vc_Settings = [[self storyboard] instantiateViewControllerWithIdentifier:@"id_vc_settings"];
    vc_Settings.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentViewController:vc_Settings animated:YES completion:nil];
}

-(void)initButtons {
    UIBarButtonItem *settings = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"gear.png"] style:UIBarButtonItemStylePlain target:self action:@selector(settingsButtonPressed:)];
    UIBarButtonItem *refresh = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"reload.png"] style:UIBarButtonItemStylePlain target:self action:@selector(refreshData:)];
    self.navigationItem.rightBarButtonItem = settings;
    self.navigationItem.leftBarButtonItem = refresh;
}

#pragma mark - Table view delegate

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if(indexPath.row == 0) {
        TVC_ScheduleTeamSelector *tvc_ScheduleTeamSelector = [self.storyboard instantiateViewControllerWithIdentifier:@"id_tvc_scheduleTeamSelector"];
        [self.navigationController pushViewController:tvc_ScheduleTeamSelector animated:YES];
    }
    
    if(indexPath.row == 1) {
        TVC_ScheduleYourTeamSelector *tvc_ScheduleYourTeamSelector = [self.storyboard instantiateViewControllerWithIdentifier:@"id_tvc_scheduleYourTeamsSelector"];
        [self.navigationController pushViewController:tvc_ScheduleYourTeamSelector animated:YES];
    }
    
    if(indexPath.row == 2 || indexPath.row == 3 || indexPath.row == 4) {
        VC_ScheduleGames *vc_ScheduleGames = [self.storyboard instantiateViewControllerWithIdentifier:@"id_vc_scheduleGames"];
        
        if(indexPath.row == 2)
            vc_ScheduleGames.dataToDisplay = @"AllGames";
        if(indexPath.row == 3)
            vc_ScheduleGames.dataToDisplay = @"Today";
        if(indexPath.row == 4)
            vc_ScheduleGames.dataToDisplay = @"Playoffs";
        
        [self.navigationController pushViewController:vc_ScheduleGames animated:YES];
    }
}

@end